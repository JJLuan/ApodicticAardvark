from flask import Flask, url_for, request, g, session, redirect, escape, flash, jsonify, Response
from werkzeug import check_password_hash, generate_password_hash
from date_convert import *
import json
app = Flask(__name__)
db = {}
message_db = [{'Time': 'Oct 10, 2015 20:31', 'Description': 'Test1', 'Location': {'a': {'city': 'Berkeley', 'text': 'South Dr, Berkeley, CA 94720, United States', 'postalCode': '94720', 'district': 'University of California Berkeley', 'countryCode': 'USA', 'additionalData': {'StateName': 'California', 'CountyName': 'Alameda', 'PostalCodeType': 'U'}, 'country': 'United States', 'street': 'South Dr', 'state': 'CA', 'county': 'Alameda'}}, 'Title': 'One'}, {'Date': 'Oct 10, 2015 9:58:00 PM', 'Author': 'test', 'Participants': 1, 'Description': 'Test', 'Location': {'a': {'text': 'South Dr, Berkeley, CA 94720, United States', 'city': 'Berkeley', 'district': 'University of California Berkeley', 'additionalData': {'StateName': 'California', 'CountyName': 'Alameda', 'PostalCodeType': 'U'}, 'country': 'United States', 'street': 'South Dr', 'countryCode': 'USA', 'postalCode': '94720', 'state': 'CA', 'county': 'Alameda'}}, 'Title': 'C'}]


@app.route('/')
def index():
	if 'username' in session:
		return 'Logged in as %s' % escape(session['username'])
	return 'You are not logged in'

@app.route('/register/', methods = ['POST'])
def register():
	if 'username' in session:
		return 'already registered'
	if request.method == 'POST':
		userin = request.get_json(force = True)
		db[userin['username']] = {'id': str(len(db.keys())),'username':userin['username'], 'pw_hash': generate_password_hash(userin['password'])}
		print('You are now registered.')
		print(db)
		return 'registered'


@app.route('/login/', methods=['POST'])
def login():
	if 'username' in session:
		return 'already signed in'
	userin = request.get_json(force = True)	
	if userin['username'] in db:
		if not check_password_hash(db[userin['username']]['pw_hash'], userin['password']):
			return 'wrong password'
		session['username'] = userin['username']
		print(session)
		return 'logged in'
	else:
		return 'no such username'

@app.route('/logout/')
def logout():
    # remove the username from the session if it's there
    print(session)
    session.pop('username', None)
    print(session)
    return 'you are logged out'
# set the secret key.  keep this really secret:
app.secret_key = 'A0Zr98j/3yX R~XHH!jmN]LWX/,?RT'

#message
@app.route('/board/', methods = ['GET', 'POST'])
def board():
	if request.method == 'POST':  #search
		return 
	return jsonify(results=message_db)

@app.route('/receive/', methods = ['POST'])
def post():
	new = request.get_json(force = True)
	message_db.append(new)
	print(new)
	print(message_db)
	return 'ok'

@app.route('/join', methods = ['POST'])
def join():
	request_title = request.get_json(force = True)
	for post in message_db:
		if not post['Title'] == request_title['Title']:
			return 'the post you are looking for does not exist'
		elif expire(post['Time']):
			message_db.remove(post)
			return 'the post you are looking for has expired'
		else:
			pass # add in the user
	return 'no post right now'

if __name__ == '__main__':
    app.run(debug = True, host = '0.0.0.0')