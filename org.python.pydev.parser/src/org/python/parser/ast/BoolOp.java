// Autogenerated AST node
package org.python.parser.ast;
import org.python.parser.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class BoolOp extends exprType implements boolopType {
    public int op;
    public exprType[] values;

    public BoolOp(int op, exprType[] values) {
        this.op = op;
        this.values = values;
    }

    public BoolOp(int op, exprType[] values, SimpleNode parent) {
        this(op, values);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("BoolOp[");
        sb.append("op=");
        sb.append(dumpThis(this.op, boolopType.boolopTypeNames));
        sb.append(", ");
        sb.append("values=");
        sb.append(dumpThis(this.values));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(29, ostream);
        pickleThis(this.op, ostream);
        pickleThis(this.values, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitBoolOp(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null)
                    values[i].accept(visitor);
            }
        }
    }

}
