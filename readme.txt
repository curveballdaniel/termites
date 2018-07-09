Daniel Peter

If you would like to use the code, please make sure to uncomment the advance you would like to use in step (shown below).
To use the normal advance, uncomment advance, but comment everything else. 
(i.e. if you would like to run spiralAdvance(), comment advance(), and uncomment spiralAdvance()

""
	@Override
	public void step(int numSteps) {
		// TODO Auto-generated method stub
		for (int i = 0; i < numSteps; i++){
			// advance termite
			advance(antposition, imagearray);
			
			// advance our spiral termite
			//spiralAdvance(antposition, imagearray);
			
			// advance our wacko random termite
			//ruleAdvance(imagearray, antposition, ruleset, state, colorarray);
		}
		image.markComplete();
	}
""

For spiral advance, the same. 

For ruleAdvance, the same too, but in this case, termite.createRuleSets() MUST be called in the client code. Otherwise there will be a null ruleset.