/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_leftStick;
  private Joystick m_rightStick;
  Solenoid piston= new Solenoid(20, 0);
  Compressor com;
  boolean hardOn;
  @Override
  public void robotInit() {
    m_myRobot = new DifferentialDrive(new Talon(0), new Talon(4));
    m_leftStick = new Joystick(0);
    m_rightStick = new Joystick(1);
    com = new Compressor(20);
    piston = new Solenoid(20, 0);

  }
  public void pistOn(){
    if(m_leftStick.getRawButtonReleased(1)){
      hardOn=!hardOn;
    }
    piston.set(hardOn);
  }

  @Override
  public void teleopPeriodic() {
    m_myRobot.tankDrive(m_leftStick.getRawAxis(3), m_leftStick.getRawAxis(1));
  }
}
