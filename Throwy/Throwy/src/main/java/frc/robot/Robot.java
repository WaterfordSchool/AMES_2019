/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;

import java.sql.Time;

import edu.wpi.first.networktables.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  Talon Left = new Talon(0);
  Talon Right = new Talon(3);
  //Talon LEDS = new Talon(9);
  Talon Shooter = new Talon(4);
  Talon Feeder = new Talon(5);
  DifferentialDrive dT = new DifferentialDrive(Right, Left);
  Joystick driver = new Joystick(0);
  Joystick op = new Joystick(1);
  JoystickButton button7 = new JoystickButton(driver, 7);
  JoystickButton button8 = new JoystickButton(driver, 8);
  public double speed = 0.8;
  double shooterStatus = 0;
  double feederStatus = 0;
  //LL Values
  double tv;
  double tx;
  double ty;
  double ta;
  boolean arcade;
  boolean target=false;
  double drive = 0.0;
  double steer = 0.0;
  double autoStartTime;


  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    
    autoStartTime = Timer.getFPGATimestamp();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    double currentTime = Timer.getFPGATimestamp();
    double timeElapsed = currentTime - autoStartTime;
    if(timeElapsed < 1.5){
      dT.tankDrive(speed, speed);
    }
    else if(timeElapsed < 14){
      Shooter.set(0.9);
      Feeder.set(0.4);
    }
    /*else if(timeElapsed < 14){
      Feeder.set(0.6);
    }
    */
    else {
      dT.tankDrive(0.0, 0.0);
      Shooter.set(0.0);
      Feeder.set(0.0);
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //dT.tankDrive(driver.getRawAxis(1) * -speed, driver.getRawAxis(3) * -speed);
    if(op.getRawButton(7) == true) shooterStatus = 1;
      else shooterStatus = 0;
    
    if(op.getRawButton(8) == true) feederStatus = 0.75;
      else feederStatus = 0;
    
    Shooter.set(shooterStatus);
    Feeder.set(feederStatus);

    LL();
    
    if(driver.getRawButton(1)){
      if(target){
        dT.arcadeDrive(0.0, steer); //insert shooter code here
        if(!target){
          Feeder.set(0.5);
          Shooter.set(1.0);
        }
      }
    }
    else{
      dT.tankDrive(-driver.getRawAxis(1) * speed, -driver.getRawAxis(3) * speed);
    }

    //fast button
    if (driver.getRawButtonPressed(7) || driver.getRawButtonPressed(8)){
      speed = 1.0;
    }
    else{
      speed = 0.8;
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  //Limelight Method
  public void LL(){
    final double STEER = 0.03;
    final double DRIVE = 0.26;
    final double AREA = 2.0; //area
    final double MAXDRIVE = 0.7;
 
    tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
 
    if(tv < 1.0){
      target = false;
      drive = 0.0;
      steer = 0.0;
    } else {
     target = true;
     steer = tx * STEER;
     drive = (AREA - ta)*DRIVE; 
 
     if(drive > MAXDRIVE){
       drive = MAXDRIVE;
     }
 
     }
   }

}
