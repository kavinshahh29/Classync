from flask import Flask
from routes.evaluation_routes import evaluation_bp

app = Flask(__name__)
app.register_blueprint(evaluation_bp, url_prefix="/api/evaluate")

if __name__ == "__main__":
    app.run(debug=True, port=5000)
