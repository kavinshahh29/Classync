from services.text_similarity import calculate_similarity
from services.grammar_checker import count_grammar_mistakes
from services.text_summarizer import get_key_points

def evaluate_submission(student_text, solution_text):
    # Get key points
    teacher_key_points = get_key_points(solution_text)
    student_key_points = get_key_points(student_text)

    # Similarities
    similarity_full = calculate_similarity(student_text, solution_text)
    similarity_to_key_points = calculate_similarity(student_text, teacher_key_points)
    similarity_key_points_to_key_points = calculate_similarity(student_key_points, teacher_key_points)

    # Weighted similarity
    weighted_similarity = (0.5 * similarity_full) + (0.3 * similarity_to_key_points) + (0.2 * similarity_key_points_to_key_points)

    # Grammar issues
    grammar_issues = count_grammar_mistakes(student_text)
    words_count = max(1, len(student_text.split()))
    normalized_grammar_penalty = min(1, grammar_issues / words_count)  # cap at 1

    # Scoring
    content_score = weighted_similarity * 90
    grammar_score = (1 - normalized_grammar_penalty) * 10
    total_score = content_score + grammar_score
    total_score = max(0, min(100, total_score))

    # Feedback
    feedback = "Excellent work!" if total_score > 90 else ("Good job, but needs slight improvement." if total_score > 75 else "Needs more improvement.")
    if grammar_issues > 5:
        feedback += " Also, please review grammar."

    return round(total_score, 2), feedback
