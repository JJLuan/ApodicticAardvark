from flask import Flask, url_for, request, g, session, redirect, escape, flash, jsonify
app = Flask(__name__)

message_db = []

@app.route('/board/')
def board():
	if not message_db:
		return 'No message yet'
	return [jsonify(**m) for m in message_db]

@app.route('/post/', methods = ['POST', 'GET'])
def post():
	if request.method == 'POST':
		new = request.get_json(force = True)
		message_db.append(new)
		print(new)
		return redirect(url_for(board))

if __name__ == '__main__':
    app.run(debug = False, host = '0.0.0.0')