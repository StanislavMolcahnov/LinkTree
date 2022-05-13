import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import static java.lang.Thread.sleep;

public class LinkTreeToFile extends RecursiveAction {
    private List<LinkTreeToFile> taskList = new ArrayList<>();
    private LinkNode node;

    public LinkTreeToFile(LinkNode node) {
        this.node = node;
    }

    @Override
    protected void compute() {
        try {
            sleep(100);
            Document linkTreeWebsite = Jsoup.connect(node.getUrl())
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .get();
            Elements links = linkTreeWebsite.select("a");
            for (Element link : links) {
                String correctLink = link.absUrl("href");
                if (!correctLink.startsWith(node.getUrl()) || correctLink.equals(node.getUrl())
                        || correctLink.contains("#") || correctLink.contains("?")) {
                    continue;
                }
                if (correctLink.endsWith("/")) {
                    correctLink = correctLink.substring(0, correctLink.length() - 1);
                }
                node.addChild(new LinkNode(correctLink));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (LinkNode child : node.getChildren()) {
            LinkTreeToFile task = new LinkTreeToFile(child);
            task.fork();
            taskList.add(task);
        }
        for (LinkTreeToFile task : taskList) {
            task.join();
        }
    }
}
