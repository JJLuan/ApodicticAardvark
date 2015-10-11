from flask import Flask, url_for, request, session, redirect, escape, flash, jsonify, Response
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

@app.route('/register', methods = ['POST'])
def register():
	if 'username' in session:
		return 'already registered'
	if request.method == 'POST':
		userin = request.get_json(force = True)
		with open("data.json", "r") as fp:
			db = json.load(fp)
		if userin['username'] in db:
			return 'username already exist. choose another one.'
		db[userin['username']] = {'username':userin['username'], 'pw_hash': generate_password_hash(userin['password']), 'rating': 3, 'currentEvents': [], 'pastEvents': []}
		with open("data.json", "w") as fp:
			json.dump(db, fp)
		print('You are now registered.')
		print(db)
		return 'registered'


@app.route('/login', methods=['POST'])
def login():
	if 'username' in session:
		return 'already signed in'
	userin = request.get_json(force = True)	
	with open("data.json", "r") as fp:
			db = json.load(fp)
	if userin['username'] in db:
		if not check_password_hash(db[userin['username']]['pw_hash'], userin['password']):
			return 'wrong password'
		session['username'] = userin['username']
		print(session)
		return 'logged in'
	else:
		return 'no such username'

@app.route('/logout')
def logout():
    # remove the username from the session if it's there
    if 'username' in session:
    	print(session)
    	session.pop('username', None)
    	print(session)
    	return 'you are logged out'
    return 'you are not logged in'

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
	#with open("message_data.json", "r") as fp:
	#	message_db = json.load(fp)
	new = request.get_json(force = True)
	new['Participants_list'] = []
	message_db.append(new)
	with open("message_data.json", "w") as fp:
			json.dump(message_db, fp)
	print(new)
	print(message_db)
	return 'ok'

@app.route('/join', methods = ['POST'])
def join():
	request_join = request.get_json(force = True)
	with open("message_data.json", "r") as fp:
		message_db = json.load(fp)
	for post in message_db:
		if not post['Title'] == request_join['Title']:
			return 'the post you are looking for does not exist'
		elif expire(post['Time']):
			message_db.remove(post)
			with open("message_data.json", "w") as fp:
				json.dump(message_db, fp)
			return 'the post you are looking for has expired'
		else:
			if len(post['Participants_list']) < post['Participants']:
				post['Participants_list'].append(request_join['Name'])
				db[session['username']]['currentEvents'].append(post)
				print('successfully joined')
				if len(post['Participants_list'] == post['Participants']):
					message_db.remove(post)
					with open("message_data.json", "w") as fp:
						json.dump(message_db, fp)
					return 'post complete'
				return 'successfully joined'
	return 'no post right now'

if __name__ == '__main__':
    app.run(debug = True, host = '0.0.0.0')