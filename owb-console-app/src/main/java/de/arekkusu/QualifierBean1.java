package de.arekkusu;

import de.arekkusu.bindings.TestQualifier;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Named;

@TestQualifier
@Named
public class QualifierBean1
{
    @ActivateRequestContext
    public void writeToConsole()
    {
        System.out.println("Write stuff to console");
    }
}
