/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
 Talon r1 = new Talon(0);
 Talon r2 = new Talon(1);
 Talon r3 = new Talon(2);
 Talon l1 = new Talon(3);
 Talon l2 = new Talon(4);
 Talon l3 = new Talon(5);
 SpeedControllerGroup r = new SpeedControllerGroup(r1, r2, r3); 
 SpeedControllerGroup l = new SpeedControllerGroup(l1, l2, l3);
 DifferentialDrive dT = new DifferentialDrive(l, r); 
 public DriveTrain(){

 }
 public void drive(double left, double right){
  dT.tankDrive(left, right);
 }
 public void drive(Joystick j, double speed){
   if(j.getAxisCount()!=6){
    if(!j.getRawButton(8)&&!j.getRawButton(7)){
      drive(speed*j.getRawAxis(3), speed*j.getRawAxis(1)); //gucci line
    }else if(j.getRawButton(8)){
     drive(speed, speed);
    }else if(j.getRawButton(7)){
      drive(-speed,-speed);
    }
   }else if(j.getRawAxis(2)==0&&j.getRawAxis(3)==0){
     drive(speed*j.getRawAxis(1), speed*j.getRawAxis(5));
   }else if(j.getRawAxis(2)>j.getRawAxis(3)){
    drive(-speed*j.getRawAxis(2), -speed*j.getRawAxis(2));
   }else if(j.getRawAxis(2)<j.getRawAxis(3)){
    drive(speed*j.getRawAxis(3), speed*j.getRawAxis(3));
   }

   
}
 public void wall(){

 }
 
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new Drive(0.8));
  }
}
