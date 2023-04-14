package dtu.mennekser.softwarehusetas.backend.data;

import java.io.Serializable;
import java.util.Arrays;

public class Join<L extends Serializable,R extends  Serializable> implements Serializable {
    public L left;
    public R right;
    public Join(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (right.getClass().isArray()) {
            return left + " -> " + Arrays.toString((Object[]) right);
        }

        return left + " -> " + right;
    }
}
