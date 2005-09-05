// Autogenerated AST node
package org.python.parser.ast;
import org.python.parser.SimpleNode;
import java.io.DataOutputStream;
import java.io.IOException;

public class While extends stmtType {
    public exprType test;
    public stmtType[] body;
    public stmtType[] orelse;

    public While(exprType test, stmtType[] body, stmtType[] orelse) {
        this.test = test;
        this.body = body;
        this.orelse = orelse;
    }

    public While(exprType test, stmtType[] body, stmtType[] orelse,
    SimpleNode parent) {
        this(test, body, orelse);
        this.beginLine = parent.beginLine;
        this.beginColumn = parent.beginColumn;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("While[");
        sb.append("test=");
        sb.append(dumpThis(this.test));
        sb.append(", ");
        sb.append("body=");
        sb.append(dumpThis(this.body));
        sb.append(", ");
        sb.append("orelse=");
        sb.append(dumpThis(this.orelse));
        sb.append("]");
        return sb.toString();
    }

    public void pickle(DataOutputStream ostream) throws IOException {
        pickleThis(15, ostream);
        pickleThis(this.test, ostream);
        pickleThis(this.body, ostream);
        pickleThis(this.orelse, ostream);
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitWhile(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (test != null)
            test.accept(visitor);
        if (body != null) {
            for (int i = 0; i < body.length; i++) {
                if (body[i] != null)
                    body[i].accept(visitor);
            }
        }
        if (orelse != null) {
            for (int i = 0; i < orelse.length; i++) {
                if (orelse[i] != null)
                    orelse[i].accept(visitor);
            }
        }
    }

}
