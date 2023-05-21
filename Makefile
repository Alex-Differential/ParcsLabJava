all: run

clean:
	rm -f out/Solver.jar out/ShanksAlgorithm.jar

out/Solver.jar: out/parcs.jar src/Solver.java
	@javac -cp out/parcs.jar src/Solver.java
	@jar cf out/Solver.jar -C src Solver.class -C src
	@rm -f src/Solver.class

out/ShanksAlgorithm.jar: out/parcs.jar src/ShanksAlgorithm.java
	@javac -cp out/parcs.jar src/ShanksAlgorithm.java
	@jar cf out/ShanksAlgorithm.jar -C src ShanksAlgorithm.class -C src
	@rm -f src/ShanksAlgorithm.class

build: out/Solver.jar out/ShanksAlgorithm.jar

run: out/Solver.jar out/ShanksAlgorithm.jar
	@cd out && java -cp 'parcs.jar:Solver.jar' Solver