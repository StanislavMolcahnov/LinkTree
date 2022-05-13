import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a link:");
        LinkNode linkRoot = new LinkNode(scanner.nextLine());
        new ForkJoinPool().invoke(new LinkTreeToFile(linkRoot));

        FileOutputStream stream = new FileOutputStream("src/main/resources/A1.txt");
        String result = createSitemapString(linkRoot, 0);
        stream.write(result.getBytes());
        stream.flush();
        stream.close();
    }

    public static String createSitemapString(LinkNode node, int depth) {
        String tabs = String.join("", Collections.nCopies(depth, "\t"));
        StringBuilder result = new StringBuilder(tabs + node.getUrl());
        node.getChildren().forEach(child -> {
            result.append("\n").append(createSitemapString(child, depth + 1));
        });
        return result.toString();
    }
}
