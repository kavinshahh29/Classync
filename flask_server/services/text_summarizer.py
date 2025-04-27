from transformers import pipeline

summarizer = pipeline("summarization", model="facebook/bart-large-cnn")

def get_key_points(text):
    max_len = min(150, int(len(text.split()) * 0.5))
    summary = summarizer(text, max_length=max_len, min_length=30, do_sample=False)
    return summary[0]["summary_text"]
