fout=open('aa.txt','w');
l=[];
for line in open('mobile_table.txt'):
	l.append(line);
l=list(set(l));
l.sort();
for i in l:
	fout.write(i);

