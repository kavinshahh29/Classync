from flask import Blueprint, request, jsonify
import requests
import chromadb
import uuid
import os

chatbot_bp = Blueprint("chatbot", __name__)
chroma_client = chromadb.PersistentClient(path="./chroma_db")
collection = chroma_client.get_or_create_collection("classroom_knowledge")

MISTRAL_API_KEY = os.getenv("MISTRAL_API_KEY")
MISTRAL_API_URL = "https://api.mistral.ai/v1/chat/completions"

@chatbot_bp.route("/train", methods=["POST"])
def train_chatbot():
    """Stores classroom knowledge in vector DB."""
    data = request.json
    class_id = str(data.get("classId"))
    title = data.get("title")
    content = data.get("content")

    if not class_id or not content:
        return jsonify({"error": "Missing required fields"}), 400

    unique_id = str(uuid.uuid4()) 
    collection.add(
        ids=[unique_id], 
        documents=[f"{title}: {content}"], 
        metadatas=[{"class_id": class_id, "title": title}]
    )

    return jsonify({"message": f"Chatbot trained successfully for class {class_id}"}), 200

@chatbot_bp.route("/respond", methods=["POST"])
def chatbot_response():
    """Answers queries based on class materials stored in ChromaDB."""
    data = request.json
    class_id = str(data.get("classId"))
    question = data.get("question")

    if not class_id or not question:
        return jsonify({"error": "Missing required fields"}), 400

    # Retrieve relevant materials from ChromaDB
    results = collection.query(query_texts=[question], n_results=3)

    if not results["metadatas"] or not results["metadatas"][0]:
        return jsonify({"response": "I don't have enough knowledge on this topic yet."}), 200

    # Extract and limit relevant data
    relevant_data = " ".join(results["documents"][0][:500]) 

    # print(f"Relevant data: {relevant_data}")

    headers = {
        "Authorization": f"Bearer {MISTRAL_API_KEY}",
        "Content-Type": "application/json"
    }

    payload = {
        "model": "mistral-medium",
        "messages": [
            {
                "role": "system",
                "content": (
                    f"You are an AI tutor for class {class_id}. "
                    "Answer concisely, providing only the most relevant details."
                )
            },
            {
                "role": "user",
                "content": (
                    f"Based on this class material: {relevant_data}, "
                    f"answer the following question briefly and to the point: {question}"
                )
            }
        ],
        "temperature": 0.7,
        "max_tokens": 150 
    }

    response = requests.post(MISTRAL_API_URL, json=payload, headers=headers)

    if response.status_code == 200:
        answer = response.json()["choices"][0]["message"]["content"]
        return jsonify({"response": answer.strip()}), 200
    else:
        return jsonify({"error": "Failed to fetch response from Mistral AI"}), 500
