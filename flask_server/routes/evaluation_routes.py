import requests
from flask import Blueprint, request, jsonify
from services.evaluation import evaluate_submission
from services.pdf_extractor import extract_text_from_pdf

evaluation_bp = Blueprint("evaluation", __name__)

def download_pdf(url, save_path):
    """Downloads a PDF file from a given URL and saves it locally."""
    response = requests.get(url)
    if response.status_code == 200:
        with open(save_path, "wb") as f:
            f.write(response.content)
    else:
        raise Exception(f"Failed to download file from {url}")

@evaluation_bp.route("/submission", methods=["POST"])
def evaluate():
    print("Evaluating submission")
    try:
        data = request.get_json()
        student_pdf_url = data["submittedFileUrl"]
        solution_pdf_url = data["solutionFileUrl"]

        # Temporary file paths
        student_pdf_path = "temp_student.pdf"
        solution_pdf_path = "temp_solution.pdf"

        # Download PDFs from Firebase
        download_pdf(student_pdf_url, student_pdf_path)
        download_pdf(solution_pdf_url, solution_pdf_path)

        # Extract text from PDFs
        student_text = extract_text_from_pdf(student_pdf_path)
        solution_text = extract_text_from_pdf(solution_pdf_path)

        # Evaluate submission
        final_score, feedback = evaluate_submission(student_text, solution_text)

        return jsonify({
            "score": final_score,
            "feedback": feedback
        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500
