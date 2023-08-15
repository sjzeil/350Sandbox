all: soundex.jar

soundex.jar: bin/edu/odu/cs/cs350/NearHomophones.class \
             bin/edu/odu/cs/cs350/SoundEx.class \
			 bin/edu/odu/cs/cs350/Substitutions.class \
			 bin/edu/odu/cs/cs350/TestSoundEx.class 
	cd bin; jar --create --file ../$@ .

bin/edu/odu/cs/cs350/%.class: src/main/java/edu/odu/cs/cs350/%.java
	javac -g -cp bin:src/main/java -sourcepath src/main/java -d bin $<

clean:
	-rm -rf bin *.jar 

test: soundex.jar
	java -cp soundex.jar edu.odu.cs.cs350.TestSoundEx
	echo ---
	java -cp soundex.jar edu.odu.cs.cs350.NearHomophones 

