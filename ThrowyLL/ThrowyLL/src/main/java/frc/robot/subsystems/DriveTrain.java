/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.Drive;

/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  Talon r = new Talon(0);
  Talon l = new Talon(3);

  DifferentialDrive dD = new DifferentialDrive(l, r);
  public DriveTrain(){
  }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new Drive(0.8));
  }
  public void drive(double l, double r){
    dD.tankDrive(l, -r);
  }
  public void drive(Joystick driver, double speed){
    dD.tankDrive(-driver.getRawAxis(1)*speed, -driver.getRawAxis(3)*speed);
  }
}
