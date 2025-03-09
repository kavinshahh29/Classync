from sentence_transformers import SentenceTransformer, util

model = SentenceTransformer("paraphrase-MiniLM-L6-v2")

def calculate_similarity(text1, text2):
    emb1 = model.encode(text1, convert_to_tensor=True)
    emb2 = model.encode(text2, convert_to_tensor=True)
    similarity = util.pytorch_cos_sim(emb1, emb2).item()
    return round(similarity, 2)
