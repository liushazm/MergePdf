import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MergePdf {

    public static String PATH;

    public static void main(String[] args) {
        PATH = System.getProperty("user.dir");
        System.out.println(PATH);

        mergePdf();
    }

    public static void mergePdf() {
        List<String> fileList = new ArrayList<String>();
        File folder = new File(PATH);
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("文件夹为空");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                String path = file.getAbsolutePath();
                String suffix = path.substring(path.lastIndexOf(".") + 1);
                if ("pdf".equals(suffix)) {
                    fileList.add(path);
                    System.out.println(path);
                }
            }
        }

        if (fileList.size() == 0) {
            System.out.println("文件夹内没有pdf文件");
            return;
        }

        File mergeFolder = new File(PATH + "/merge/");
        if (mergeFolder.exists()) {
            System.out.println("merge文件夹已存在");
            return;
        }

        mergeFolder.mkdirs();

        Document document = null;
        try {
            document = new Document(new PdfReader(fileList.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(mergeFolder + "/merge.pdf"));
            document.open();
            for (int i = 0; i < fileList.size(); i++) {
                PdfReader reader = new PdfReader(fileList.get(i));
                int n = reader.getNumberOfPages();// 获得总页码
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);// 从当前Pdf,获取第j页
                    copy.addPage(page);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

}
