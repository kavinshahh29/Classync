from flask import Flask
from routes.evaluation_routes import evaluation_bp 
from routes.chatbot_routes import chatbot_bp
from flask_cors import CORS


app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "http://localhost:5173"}})
app.register_blueprint(evaluation_bp, url_prefix="/api/evaluate")
app.register_blueprint(chatbot_bp, url_prefix="/api/chatbot")

if __name__ == "__main__":
    app.run(debug=True, port=5000)
