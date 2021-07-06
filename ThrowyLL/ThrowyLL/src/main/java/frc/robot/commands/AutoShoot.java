/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class AutoShoot extends Command {
  public AutoShoot() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.shooter);
    requires(Robot.feeder);
  }
  boolean m;
  double t;
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.shooter.shoot();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
   for(int i = 0;i<5;i++){
    Robot.feeder.allow();
   }
   m=true;
   t=timeSinceInitialized();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(m){
      return t>3.5;
    }else return m;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.shooter.stop();
    Robot.feeder.deny();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
