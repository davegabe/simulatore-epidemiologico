import java.util.ArrayList;

public class QuadTree {
    static final int maxSize = 2;
    static final int maxSubdivision = 10;
    public QuadTree[] children;
    public ArrayList<Person> people = new ArrayList<>();
    private int subdivision;
    private int x1, x2, y1, y2;

    QuadTree(int x1, int x2, int y1, int y2, int subdivision) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.subdivision = subdivision + 1;
    }

    public void clear() {
        people.clear();

        if (children == null)
            return;
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                children[i].clear();
            }
        }
        children = null;
    }

    private int getIndex(Person person) {
        int index = -1;
        int midx = x1 + (x2 - x1) / 2;
        int midy = y1 + (y2 - y1) / 2;

        boolean topQuadrant = (person.y - person.r >= y1 && person.y + person.r < midy);  // if it's in the upper half
        boolean bottomQuadrant = (person.y - person.r >= midy && person.y + person.r < y2);                             // if it's in the bottom half

        if (person.x - person.r >= x1 && person.y + person.r < midx) {                    //if it's in the left half
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (person.y - person.r >= midx && person.x + person.r < x2) {                                           //if it's in the right half
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    public void retrieve(ArrayList<Person> returnObjects, Person person) {
        if (children != null) {
            int index = getIndex(person);
            if (index != -1) {
                children[index].retrieve(returnObjects, person);
            } else {
                for (int i = 0; i < children.length; i++) {
                    children[i].retrieve(returnObjects, person);
                }
            }
        }
        returnObjects.addAll(people);
    }

    private void split() {
        int midx = x1 + (x2 - x1) / 2;
        int midy = y1 + (y2 - y1) / 2;
        if (children == null) {
            children = new QuadTree[4];
            children[0] = new QuadTree(x1, midx, y1, midy, subdivision);
            children[1] = new QuadTree(midx, x2, y1, midy, subdivision);
            children[2] = new QuadTree(x1, midx, midy, y2, subdivision);
            children[3] = new QuadTree(midx, x2, midy, y2, subdivision);
        }
    }

    public void insert(Person person) {
        if (children != null) {
            int index = getIndex(person);
            if (index != -1) {
                children[index].insert(person);
                return;
            }
        }

        people.add(person);

        if (people.size() >= maxSize - 1 && subdivision <= maxSubdivision) {  //if there are too many players or is not a leaf and the division is bigger than the Player dimension, move them deeper
            if (children == null) {
                split();
            }

            int i = 0;
            while (i < people.size()) {
                int index = getIndex(people.get(i));
                if (index != -1) {
                    children[index].insert(people.remove(i));
                } else {
                    i++;
                }
            }
        }
    }
}