#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import net.sourceforge.seqware.common.module.FileMetadata;
import net.sourceforge.seqware.common.module.ReturnValue;
import net.sourceforge.seqware.common.util.filetools.FileTools;
  
import net.sourceforge.seqware.pipeline.module.Module;
import net.sourceforge.seqware.common.util.runtools.RunTools;

import org.openide.util.lookup.ServiceProvider;


/**
 * This is a hello world module that should give you an idea of how
 * to create a module of your own. It uses the same utilities and syntax
 * as a real module.
 * 
 * Please use JavaDoc to document each method (the user interface documents
 * will be autogenerated using these comments).  See 
 * http://en.wikipedia.org/wiki/Javadoc for more information.
 * 
 * @author briandoconnor@gmail.com
 *
 */
@ServiceProvider(service=ModuleInterface.class)
public class HelloWorldExample extends Module {

  private OptionSet options = null;
  private File tempDir = null;

  /**
   * getOptionParser is an internal method to parse command line args.
   * 
   * @return OptionParser this is used to get command line options
   */
  protected OptionParser getOptionParser() {
    OptionParser parser = new OptionParser();
    parser.accepts("greeting", "A greeting that will be echoed back in the output file, the number of times depends on the repeat param.").withRequiredArg().ofType(String.class).describedAs("required");
    parser.accepts("repeat", "How many times to repeat greeting").withRequiredArg().ofType(Integer.class).describedAs("required");
    parser.accepts("input-file", "This is the input file name to cat before addition to gretting").withRequiredArg().ofType(String.class).describedAs("optional");
    parser.accepts("output-file", "This is the output file name").withRequiredArg().ofType(String.class).describedAs("required");
    parser.accepts("echo-binary-path", "Option path to the echo binary").withRequiredArg().ofType(String.class).describedAs("optional");
    parser.accepts("cat-binary-path", "Option path to the cat binary").withRequiredArg().ofType(String.class).describedAs("optional");

    return (parser);
  }

  /**
   * A method used to return the syntax for this module
   * @return a string describing the syntax
   */
  @Override
  public String get_syntax() {
    OptionParser parser = getOptionParser();
    StringWriter output = new StringWriter();
    try {
      parser.printHelpOn(output);
      return(output.toString());
    } catch (IOException e) {
      e.printStackTrace();
      return(e.getMessage());
    }
  }

  /**
   * The init method is where you put any code needed to setup your module.
   * Here I set some basic information in the ReturnValue object which will eventually
   * populate the "processing" table in seqware_meta_db. I also create a temporary
   * directory using the FileTools object.
   * 
   * init is optional
   * 
   * @return A ReturnValue object that contains information about the status of init
   */
  @Override
  public ReturnValue init() {

    // setup the return value object, notice that we use
    // ExitStatus, this is what SeqWare uses to track the status
    ReturnValue ret = new ReturnValue();
    ret.setExitStatus(ReturnValue.SUCCESS);
    // fill in the algorithm field in the processing table
    ret.setAlgorithm("hello-world-module");
    // fill in the description field in the processing table
    ret.setDescription("This demonstrates how to write a simple module");
    // fill in the version field in the processing table
    ret.setVersion("0.7.0");
    
    try {

      OptionParser parser = getOptionParser();

      // The parameters object is actually an ArrayList of Strings created
      // by splitting the command line options by space. JOpt expects a String[]
      options = parser.parse(this.getParameters().toArray(new String[0]));

      // create a temp directory in current working directory
      tempDir = FileTools.createTempDirectory(new File("."));

      // you can write to "stdout" or "stderr" which will be persisted back to the DB
      ret.setStdout(ret.getStdout()+"Output: "+(String)options.valueOf("output-file")+"${symbol_escape}n");

    } catch (OptionException e) {
      e.printStackTrace();
      ret.setStderr(e.getMessage());
      ret.setExitStatus(ReturnValue.INVALIDPARAMETERS);
    } catch (IOException e) {
      e.printStackTrace();
      ret.setStderr(e.getMessage());
      ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
    }
    
    // now return the ReturnValue
    return (ret);

  }

  /**
   * Verifies that the parameters make sense
   * 
   * @return a ReturnValue object
   */
  @Override
  public ReturnValue do_verify_parameters() {
    
    // most methods return a ReturnValue object
    ReturnValue ret = new ReturnValue();
    ret.setExitStatus(ReturnValue.SUCCESS);
    
    // now look at the options and make sure they make sense
    for (String option : new String[] {
        "greeting", "repeat", "output-file"
      }) {
      if (!options.has(option)) {
        ret.setExitStatus(ReturnValue.INVALIDPARAMETERS);
        String stdErr = ret.getStderr();
        ret.setStderr(stdErr+"Must include parameter: --"+option+"${symbol_escape}n");
      }
    }
    
    // can pull back typed command line args
    if ((Integer)options.valueOf("repeat") < 1) {
      ret.setExitStatus(ReturnValue.INVALIDPARAMETERS);
      String stdErr = ret.getStderr();
      ret.setStderr(stdErr+"The parameter --repeat must be >= 1${symbol_escape}n");
    }

    return ret;
  }

  /**
   * The do_verify_input method ensures that the input files exist. It
   * may also do validation of the input files or anything that is needed
   * to make sure the module has everything it needs to run. There is some
   * overlap between this method and do_verify_parameters. This one is more
   * focused on validating files, making sure web services are up, DBs can be
   * connected to etc.  While do_verify_parameters is primarily used to
   * validate that the minimal parameters are passed in. The overlap between
   * these two methods is at the discretion of the developer
   * 
   * @return a ReturnValue object
   */
  @Override
  public ReturnValue do_verify_input() {
    
    // not much to do, let's make sure the
    // temp directory is writable
    ReturnValue ret = new ReturnValue();
    ret.setExitStatus(ReturnValue.SUCCESS);
    
    
    // If input-file argument was specified, make sure it exits and is readable
    if ( options.has("input-file") ) {

      ReturnValue inputRet =  FileTools.fileExistsAndReadable( new File( (String) options.valueOf("input-file") ));
      if ( inputRet.getExitStatus() != ReturnValue.SUCCESS)  {
        
        ret.setExitStatus(ReturnValue.FILENOTREADABLE);
        ret.setStderr("Can't read from input file " + (String) options.valueOf("input-file") + ": " + inputRet.getStderr() );
        return (ret);
      }
    }
    
    
    // Notice the FileTools actually returns ReturnValue objects too!
    if (FileTools.dirPathExistsAndWritable(tempDir).getExitStatus() != ReturnValue.SUCCESS) {
      ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
      ret.setStderr("Can't write to temp directory");
    }
    
    return (ret);

  }

  /**
   * This is really an optional method but a very good idea. You
   * would test the programs your calling here by running them on
   * a "known good" test dataset and then compare the new answer
   * with the previous known good answer. Other forms of testing could be
   * encapsulated here as well.
   * 
   * @return a ReturnValue object
   */
  @Override
  public ReturnValue do_test() {
    
    // notice the use of "NOTIMPLEMENTED", this signifies that we simply 
    // aren't doing this step. It's better than just saying SUCCESS
    ReturnValue ret = new ReturnValue();
    ret.setExitStatus(ReturnValue.NOTIMPLEMENTED);

    // not much to do, just return
    return(ret);
  }

  /**
   * This is the core of a module. While some modules may be written in pure Java or use
   * various third-party Java APIs, the vast majority of modules will use this method to 
   * make calls out to the shell (typically the BASH shell in Linux) and use that shell
   * to execute various commands.  In an ideal world this would never happen, we would all 
   * write out code with a language-agnostic, network-aware API (e.g. thrift, SOAP, etc).
   * But until that day comes most programs in bioinformatics are command line tools
   * (or websites). So the heart of the module is it acts as a way for us to treat the
   * disparate tools as well-behaved modules that present a standard interface
   * and report back their metadata in well-defined ways. That's, ultimately, what this
   * object and, in particular this method, are all about.
   * 
   * There are other alternatives out there, such as Galaxy, that may provide an XML
   * syntax for accomplishing much of the same thing. For example, they make disparate tools
   * appear to function the same because the inputs/outputs are all described using a standardized 
   * language. We chose Java because it was more expressive than XML as a module running
   * descriptor. But clearly there are a lot of ways to solve this problem. The key concern,
   * though, is that a module should present very clear inputs and outputs based,
   * whenever possible, on standardized file types. This makes it easy to use modules in
   * novel workflows, rearranging them as needed.  Make every effort to make your modules
   * self-contained and robust!
   * 
   * @return a ReturnValue object
   */
  @Override
  public ReturnValue do_run() {
    
    // prepare the return value
    ReturnValue ret = new ReturnValue();
    ret.setExitStatus(ReturnValue.SUCCESS);
    // track the start time of do_run for timing purposes
    ret.setRunStartTstmp(new Date());
    
    // save StdErr and StdOut
    StringBuffer stderr = new StringBuffer();
    StringBuffer stdout = new StringBuffer();
    
    int repeat = (Integer)options.valueOf("repeat");
    String greeting = (String)options.valueOf("greeting");
    String output = (String)options.valueOf("output-file");
    String echoBinary = null ;
    
    // If directory of output-file does not exist than make it
    File parentDirectory = new File( (String) options.valueOf("output-file")).getParentFile();
    if ( ! parentDirectory.exists() ) {
      parentDirectory.mkdirs();
    }
    
    if ( options.has("echo-binary-path") ) {
      echoBinary = (String)options.valueOf("echo-binary-path");
      stderr.append("Using binary " + echoBinary + "${symbol_escape}n");
    }
    else {
      echoBinary = "echo";
    }
    
    if ( options.has("input-file") ) {
      String catBinary = null;
      
      if ( options.has("cat-binary-path") ) {
        catBinary = (String)options.valueOf("cat-binary-path");
      }
      else {
        catBinary = "cat";
      }
      
      ReturnValue result = RunTools.runCommand(new String[] { "bash", "-c", catBinary + " " + (String) options.valueOf("input-file") + " >> "+output} );
      stderr.append(result.getStderr());
      stdout.append(result.getStdout());
    }
    
    for (int i=0; i<repeat; i++) {
      // This is a generally safe way to call from the command line, with an explicit shell. Here it's
      // bash. Normally, you can just call RunTools.runCommand with a string as an argument but
      // the output redirect here requires this array style of calling.

      
      ReturnValue result = RunTools.runCommand(new String[] { "bash", "-c", echoBinary + " '" + greeting+"' >> "+output} );
      stderr.append(result.getStderr());
      stdout.append(result.getStdout());
      if (result.getProcessExitStatus() !=  ReturnValue.SUCCESS || result.getExitStatus() != ReturnValue.SUCCESS) {
        ret.setExitStatus(result.getExitStatus());
        ret.setProcessExitStatus(result.getProcessExitStatus());
        ret.setStderr(stderr.toString());
        ret.setStdout(stdout.toString());
        return(ret);
      }
    }
    
    // record the file output
    FileMetadata fm = new FileMetadata();
    fm.setMetaType("text/plain");
    fm.setFilePath(output);
    fm.setType("hello-world-text-output");
    fm.setDescription("A text output for hello world.");
    ret.getFiles().add(fm);
    
    // note the time do_run finishes
    ret.setRunStopTstmp(new Date());
    return(ret);
    
  }

  /**
   * A method to check to make sure the output was created correctly
   * 
   * @return a ReturnValue object
   */
  @Override
  public ReturnValue do_verify_output() {
    
    // this is easy, just make sure the file exists
    return(FileTools.fileExistsAndReadable(new File((String)options.valueOf("output-file"))));
    
  }
  
  /**
   * A cleanup method, make sure you cleanup files that are outside the current working directory
   * since Pegasus won't clean those for you.
   * 
   * clean_up is optional
   */
  @Override
  public ReturnValue clean_up() {
    
    ReturnValue ret = new ReturnValue();
    ret.setExitStatus(ReturnValue.SUCCESS);
    
    if (!tempDir.delete()) {
      ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
      ret.setStderr("Can't delete folder: "+tempDir.getAbsolutePath());
    }
    
    return(ret);
  }
  
}
