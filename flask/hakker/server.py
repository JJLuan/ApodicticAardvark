from flask import Flask, url_for, request, g, session, redirect, escape, flash
from werkzeug import check_password_hash, generate_password_hash
app = Flask(__name__)
db = {}

def get_db():
	global db
	return db

def sumSessionCounter():
	try:
		session['counter'] += 1
	except KeyError:
		session['counter'] = 1

@app.route('/')
def index():
	if 'username' in session:
		return 'Logged in as %s' % escape(session['username'])
	return 'You are not logged in'

@app.route('/register', methods = ['GET', 'POST'])
def register():
	if 'username' in session:
		return redirect(url_for('index'))
	error = None
	if request.method == 'POST':
		db = get_db
		db[str(len(db.keys()))] = [request.form['username'], generate_password_hash(request.form['password'])]
		flash('You are now registered.')
		return redirect(url_for('login'))
	return '''
		<form action="" method="post">
			<p><input type=text name=username>
			<p><input type=text name=password>
			<p><input type=submit value=Login>
		</form>
	'''


@app.route('/login', methods=['GET', 'POST'])
def login():
	if 'username' in session:
		return redirect(url_for('index'))
	if request.method == 'POST':
		session['username'] = request.form['username']
		return redirect(url_for('index'))
	return '''
		<form action="" method="post">
			<p><input type=text name=username>
			<p><input type=submit value=Login>
		</form>
	'''

@app.route('/logout')
def logout():
    # remove the username from the session if it's there
	flash('You are logged out')
	session.pop('username', None)
	return redirect(url_for('index'))

# set the secret key.  keep this really secret:
app.secret_key = 'A0Zr98j/3yX R~XHH!jmN]LWX/,?RT'

if __name__ == '__main__':
    app.run(debug = False, host = '0.0.0.0')