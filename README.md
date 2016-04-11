# Optimisation-algorithms
PhD code

This code implements a novel optimisation algorithm which can find approximate good solutions to three permutation combinatorial problems: Quadratic Assignment Problem (QAP), Linear Ordering Problem (LOP) and Flow-Shop Scheduling Problem (PFSP).
The files are aggregated in a jar file which can be run in the command line as follows:
java -jar problem.jar propfile.xml
The properties of the problems and the parameters needed to run the code are found in the xml file (propfile).
The parameters in the xml file are:
- problem type (1 if it is a minimisation problem, -1 if it is a maximisation problem).
- objective function name (the problem that we want to optimise, e.g QAP)
- significance (the significance level for the sampling, e.g 0.02 indicates a 2% risk that better values could still be found when the sampling stops)
- z-score for significance (e.g 2.33 for significance 0.02)
- neighbourhoodMinSample (minimum number of moves performed in the neighbourhood before starting calculating when the sampling should stop)
- predictor acceptance threshold (threshold used for acceptance of a new predictor, if the predictors are too similar, the new proposal will be rejected e.g < 0.1)
