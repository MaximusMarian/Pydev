/* 
 * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler
 *
 * IFS Institute for Software, HSR Rapperswil, Switzerland
 * 
 */

package org.python.pydev.refactoring.coderefactoring.extractmethod.edit;

import java.util.ArrayList;
import java.util.List;

import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Assign;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.Call;
import org.python.pydev.parser.jython.ast.Name;
import org.python.pydev.parser.jython.ast.NameTok;
import org.python.pydev.parser.jython.ast.exprType;
import org.python.pydev.refactoring.ast.adapters.IASTNodeAdapter;
import org.python.pydev.refactoring.ast.adapters.IClassDefAdapter;
import org.python.pydev.refactoring.coderefactoring.extractmethod.request.ExtractMethodRequest;
import org.python.pydev.refactoring.core.edit.AbstractReplaceEdit;

public class ExtractCallEdit extends AbstractReplaceEdit {

    private String methodName;

    private int offset;

    private IASTNodeAdapter<?> offsetAdapter;

    private int replaceLength;

    private List<String> callParameters;

    private List<String> returnVariables;

    public ExtractCallEdit(ExtractMethodRequest req) {
        super(req);
        this.methodName = req.getMethodName();
        this.offset = req.getSelection().getOffset();

        this.replaceLength = req.getSelection().getLength();
        this.offsetAdapter = req.getOffsetNode();

        this.callParameters = req.getParameters();
        this.returnVariables = req.getReturnVariables();
    }

    @Override
    protected SimpleNode getEditNode() {

        List<exprType> argsList = initCallArguments();
        Call methodCall = new Call(createCallAttribute(), argsList.toArray(new exprType[0]), null, null, null);

        return initSubstituteCall(methodCall);

    }

    private SimpleNode initSubstituteCall(Call methodCall) {
        if(returnVariables.size() == 0){
            return methodCall;
        }else{
            List<exprType> returnExpr = new ArrayList<exprType>();
            for(String returnVar:returnVariables){
                returnExpr.add(new Name(returnVar, Name.Store, false));
            }

            return new Assign(returnExpr.toArray(new exprType[0]), methodCall);
        }
    }

    private List<exprType> initCallArguments() {
        List<exprType> argsList = new ArrayList<exprType>();
        for(String parameter:callParameters){
            argsList.add(new Name(parameter, Name.Load, false));
        }
        return argsList;
    }

    private exprType createCallAttribute() {
        if(this.offsetAdapter instanceof IClassDefAdapter){
            return new Attribute(new Name("self", Name.Load, false), new NameTok(this.methodName, NameTok.Attrib), Attribute.Load);
        }else{
            return new Name(this.methodName, Name.Load, false);
        }
    }

    @Override
    public int getOffsetStrategy() {
        return 0;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    protected int getReplaceLength() {
        return replaceLength;
    }

}
