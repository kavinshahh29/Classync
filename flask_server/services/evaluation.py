from services.text_similarity import calculate_similarity
from services.grammar_checker import count_grammar_mistakes
from services.text_summarizer import get_key_points

def evaluate_submission(student_text, solution_text):
    # Get key points from the teacher's solution
    key_points = get_key_points(solution_text)

    # Calculate similarity with both key points & full solution
    similarity_to_key_points = calculate_similarity(student_text, key_points)
    similarity_to_full_solution = calculate_similarity(student_text, solution_text)

    # Give higher priority to full solution match
    weighted_similarity = (0.7 * similarity_to_full_solution) + (0.3 * similarity_to_key_points)

    # Count grammar mistakes
    grammar_issues = count_grammar_mistakes(student_text)

    # Scoring formula
    score = (weighted_similarity * 80) - (grammar_issues * 2)
    score = max(0, min(100, score))

    # Generate feedback
    feedback = "Great job!" if score > 85 else "Needs improvement."
    if grammar_issues > 5:
        feedback += " Too many grammar issues."

    return round(score, 2), feedback
