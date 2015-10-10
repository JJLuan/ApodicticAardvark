from flask import Flask, url_for, request, g, session, redirect, escape, flash
app = Flask(__name__)

class message_db:
	def __init__(self):
		messages = []

	def __repr__(self):
		s = ''
		for m in messages:
			s += str(m) + '\n'
		return s
class message:
	def __init__(self, author_id, title, subject):
		self.author = author_id
		self.title = title
		self.subject = subject

@app.route('/')
def 

@app.route('/post')



if __name__ == '__main__':
    app.run(debug = False, host = '0.0.0.0')