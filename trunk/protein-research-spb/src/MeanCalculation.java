import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class MeanCalculation {
	public static void main(String[] args) throws IOException, ParseException {
//		String calcFolderString = "1Y8B/precise-research";
//		String calcFolderString = "1Y8B/spheres-research";
		String calcFolderString = "1Y8B/combined-research";
		File calcFolder = new File(calcFolderString);
		String[] files = calcFolder.list();
		Arrays.sort(files);

		PrintWriter pw = new PrintWriter(calcFolderString + "/meanResults_" + calcFolderString.replace("/", "_"));
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

		for (String file : files) {
			if (!(file.contains("research"))) {
				continue;
			}
			BufferedReader br = new BufferedReader(new FileReader(calcFolderString + "/" + file));
			pw.println();
			pw.println(file);
			String currentString = "";
			int count = 0;
			long sumSteps = 0;
			double sumSecs = 0;
			String token = "";
			while (br.ready()) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				token = st.nextToken();
				// if (currentString.equals("")){
				// currentString = token;
				// }
				// if (token.equals(currentString)) {
				count++;
				sumSteps += Integer.parseInt(st.nextToken());
				sumSecs += format.parse(st.nextToken()).doubleValue();
				// } else {
				// pw.println(token + " " + (sumSteps / count) + " " + (sumSecs
				// / count));
				// pw.println();
				// currentString = token;
				// }
			}
			pw.println(token + " " + (sumSteps / count) + " " + (sumSecs / count));
			br.close();
		}
		pw.close();

	}
}
