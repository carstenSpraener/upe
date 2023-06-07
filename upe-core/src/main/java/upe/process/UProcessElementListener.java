package upe.process;


import upe.process.rules.UpeRuleVetoException;

public interface UProcessElementListener {
	void elementChanged(UProcessElement abstractUProcessElementImpl) throws UpeRuleVetoException;
}
