My PhD# Predictive-Optimisation-algorithms


This sample code implements a novel optimisation algorithm which can find approximate good solutions and predict the quality of the solutions for three permutation combinatorial problems: Quadratic Assignment Problem (QAP), Linear Ordering Problem (LOP) and Flow-Shop Scheduling Problem (PFSP).
The files are aggregated in a jar file which can be run in the command line as follows:
java -jar problem.jar propfile.xml. 
The properties of the problems and the parameters needed to run the code are found in the xml file (propfile).
The parameters in the xml file are:
- problem type (1 if it is a minimisation problem, -1 if it is a maximisation problem).
- objective function name (the problem that we want to optimise, e.g QAP)
- significance (the significance level for the sampling, e.g 0.02 indicates a 2% risk that better values could still be found when the sampling stops)
- z-score for significance (e.g 2.33 for significance 0.02)
- neighbourhoodMinSample (minimum number of moves performed in the neighbourhood before starting calculating when the sampling should stop)
- predictor acceptance threshold (threshold used for acceptance of a new predictor, if the predictors are too similar, the new proposal will be rejected e.g < 0.1)
- jobs (for PFSP problems number of jobs, eg 50 jobs for the instance jc5020)
- machines (for PFSP problems number of machines, e.g 20 machines for the instance jc5020)
- matrix cardinality (for QAP and LOP problems the dimension of matrix or matrices which contains the problem description, e.g 32 for the instance esc32a)
- PinstanceName (name of the problem instance to be optimised, e.g esc32a)
- PBestName (the file containing the best solution known for that instance; on the first line is the fitness value, and on the following lines the permutation of the best solution known)
- rounds (how many rounds of the algorithm run, e.g 30 for statistical significance)
- seed (the seed for the pseudo-random generator used in the alorithm, since the heuristic algorithm incorporates a significant degree of randomness)

The output of the algorithm is collected in a result file (Results.txt) containing various statistics. For this code example are included the fitnesses found along with the permutations representing the solutions for the problems, the prediction errors for the identified solutions, which predictors were used and how large is the predictors group. Three instances are included: esc32a for QAP problem, npal23 for LOP problem and jc5020 for PFSP.

Example how to run the code in command line:

> java -jar problem.jar propfile.xml

Preloaded in the config file is the problem instace jc5020. The estimated bound for the best solution reported in the literature is found in the file named best-jc5020.txt. This is a minimisation problem and the algorithm tries to find solutions as close as possible to the estimated theoretical bound. The duration of the run for 30 rounds on my computer is approximately 1 hour, but if you can have access to a SuperComputer it will do the job, by the time that you put for a coffee :)...alternatively you can reduce the number of rounds if you don't have an hour to spare...Enjoy!
