rm ./target/* -rf
javac Test.java -d target
cp config.properties ./target/
cp util/banner.txt ./target/util/
