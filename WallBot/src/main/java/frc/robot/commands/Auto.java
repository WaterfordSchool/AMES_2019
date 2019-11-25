/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.*;
import frc.robot.Robot;

public class Auto extends Command {
  public Auto() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.dT);
  }
  Timer time = new Timer();
  double st;
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    st=Timer.getFPGATimestamp();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double t = Timer.getFPGATimestamp()-st;
    while(t<2){
      t=Timer.getFPGATimestamp()-st;
      Robot.dT.drive(0.8, 0.8);
    }
    while(t<5){
      t=Timer.getFPGATimestamp()-st;
      Robot.dT.drive(-0.8, 0.8);
    }
      Robot.dT.drive(0, 0);
    

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
