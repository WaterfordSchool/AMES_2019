/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Drive extends Command {
  Joystick d;
  double s;
  public Drive(double s) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.dT);
    this.s = s;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //d.equals(Robot.m_oi.getD());
    //drive();
    Robot.dT.drive(Robot.m_oi.getD(), s);
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
  public void drive(){
    if(d.getAxisCount()==4){
      Robot.dT.drive(-d.getRawAxis(1), -d.getRawAxis(3));
    }else if(d.getAxisCount()==6){
      Robot.dT.drive(-d.getRawAxis(1), -d.getRawAxis(5));
    }
  }
}
