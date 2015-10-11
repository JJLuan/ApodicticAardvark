from datetime import datetime
month_to_int = {'Jan':1, 'Feb':2, 'Mar':3, 'Apr':4, 'May':5, 'Jun':6, 'Jul':7, 'Aug':8, 'Sep':9, 'Oct':10, 'Nov':11, 'Dec':12}
def expire(s):
	s = s.split()
	now = datetime.now()
	sch = datetime(int(s[2]), month_to_int[s[0]], int(s[1][:2]), int(s[3][:2]), int(s[3][3:]))
	return now > sch