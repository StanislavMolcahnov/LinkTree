import java.util.ArrayList;

public class LinkNode {
    private volatile LinkNode parent;
    private String url;
    private volatile ArrayList<LinkNode> children;
    private volatile int depth;

    public LinkNode(String url) {
        depth = 0;
        parent = null;
        this.url = url;
        children = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public synchronized void addChild(LinkNode child) {
        LinkNode root = getRootElement();
        if(!root.contains(child.getUrl())) {
            child.setParent(this);
            children.add(child);
        }
    }

    private void setParent(LinkNode linkNode) {
        synchronized (this) {
            this.parent = linkNode;
            this.depth = calculateDepth();
        }
    }

    private int calculateDepth() {
        int result = 0;
        if (parent == null) {
            return result;
        }
        result = 1 + parent.calculateDepth();
        return result;
    }

    public LinkNode getRootElement() {
        return parent == null ? this : parent.getRootElement();
    }

    private boolean contains(String url) {
        if (this.url.equals(url)) {
            return true;
        }
        for (LinkNode child : children) {
            if(child.contains(url))
                return true;
        }

        return false;
    }

    public ArrayList<LinkNode> getChildren() {
        return children;
    }
}
