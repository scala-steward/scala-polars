import sbt._

import scala.sys.process._

object Utils {

  lazy val nativeRoot = taskKey[File]("Directory pointing to the native project root.")

  def executeProcesses(
      cmds: Seq[String],
      cwd: Option[File] = None,
      logger: Logger,
      infoOnly: Boolean = false,
      extraEnv: Seq[(String, String)] = Nil
  ): Unit = cmds.foreach(cmd => executeProcess(cmd, cwd, logger, infoOnly = true))

  def executeProcess(
      cmd: String,
      cwd: Option[File] = None,
      logger: Logger,
      infoOnly: Boolean = false,
      extraEnv: Seq[(String, String)] = Nil
  ): Unit = {
    val exitCode =
      Process(cmd, cwd, extraEnv: _*).run(getProcessLogger(logger, infoOnly)).exitValue()
    logger.info(s"Executed command `$cmd` with exit code $exitCode.")
  }

  def priorTo213(scalaVersion: String): Boolean =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, minor)) if minor < 13 => true
      case _ => false
    }

  def getProcessLogger(logger: Logger, infoOnly: Boolean = false): ProcessLogger =
    ProcessLogger(
      (o: String) => logger.info(o),
      (e: String) => if (infoOnly) logger.info(e) else logger.error(e)
    )

}
