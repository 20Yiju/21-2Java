package edu.handong.csee.java.chatparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;


public class ChatParser {
	
	private String path;
	private boolean verbose;
	private boolean help;
	private String date;
	private String input;
	private String output;
	private String writer;
	
	public static void main(String[] args) {
		
		ChatParser mychatparser = new ChatParser();
		mychatparser.run(args);
	}
	
	private void run(String[] args) {
		Options options = createOptions();
		
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
	
			// path is required (necessary) data so no need to have a branch.
			System.out.println("-i option is " + input);
			System.out.println("-o option is " + output);
			System.out.println("-d option is " + date);
			System.out.println("-w option is " + writer);
			
			// TODO show the number of files in the path
			
			if(verbose) {
				
				// TODO list all files in the path
				
				System.out.println("Your program is terminated. (This message is shown because you turned on -v option!");
			}
		}
	}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			//path = cmd.getOptionValue("p");
			//verbose = cmd.hasOption("v");
			help = cmd.hasOption("h");
			date = cmd.getOptionValue("d");
			input = cmd.getOptionValue("i");
			output = cmd.getOptionValue("o");
			writer = cmd.getOptionValue("w");
	

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		/*options.addOption(Option.builder("p").longOpt("path")
				.desc("Set a path of a directory or a file to display")
				.hasArg()
				.argName("Path name to display")
				.required()
				.build());
		

		// add options by using OptionBuilder
		options.addOption(Option.builder("v").longOpt("verbose")
				.desc("Display detailed messages!")
				//.hasArg()     // this option is intended not to have an option value but just an option
				.argName("verbose option")
				//.required() // this is an optional option. So disabled required().
				.build());*/
		
		options.addOption(Option.builder("d").longOpt("date")
				.desc("Apply a filter by a specific date")
				.hasArg()
				.argName("Date filter")
				//.required()
				.build());
		
		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Show a Help page")
		        .build());
		
		
		
		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input data file path for chat messages")
				.hasArg()
				.argName("Input file path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output data file path for chat messages")
				.hasArg()
				.argName("Output file path")
				.required()
				.build());
		
		options.addOption(Option.builder("w").longOpt("writer")
				.desc("Apply a filter by a specific writer")
				.hasArg()
				.argName("Writer filter")
				//.required()
				.build());

		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Chatparser program";
		String footer ="\nThis chatparser is implemented in 2021-1 Java class.";
		formatter.printHelp("chatparser", header, options, footer, true);
	}

	
	
}
