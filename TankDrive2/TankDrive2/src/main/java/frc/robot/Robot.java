/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
//Motors
  public WPI_TalonSRX leftFront = new WPI_TalonSRX(2);
  public WPI_TalonSRX leftBack = new WPI_TalonSRX(1);
  public WPI_TalonSRX rightFront = new WPI_TalonSRX(3);
  public WPI_TalonSRX rightBack = new WPI_TalonSRX(4);
//Speed Controller Groups
  public SpeedControllerGroup left = new SpeedControllerGroup(leftFront, leftBack);
  public SpeedControllerGroup right = new SpeedControllerGroup(rightFront, rightBack);
//Dif Drive
  DifferentialDrive driveTrain = new DifferentialDrive(left, right);
//OI
  public Joystick driver = new Joystick(0);
//Speed Variable
  public double speed = 0.8;
//Limelight Variables
private boolean m_LimelightHasValidTarget = false;
private double m_LimelightDriveCommand = 0.0; //z
private double m_LimelightSteerCommand = 0.0; //x

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
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

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //Check arcadeDrive method to see if it drives with those values

    Update_Limelight_Tracking();
    
    boolean llTrack = driver.getRawButton(2); 

    if(llTrack){
      if(m_LimelightHasValidTarget){
        left.setInverted(true);
        driveTrain.arcadeDrive(m_LimelightDriveCommand, m_LimelightSteerCommand); //if needed to aim horizontally, pass 0.0 for drive command
      }
      else{
        driveTrain.arcadeDrive(0.0, 0.0);
      }
    }
    else{
      left.setInverted(false);
      driveTrain.tankDrive(driver.getRawAxis(1) * speed, -driver.getRawAxis(3) * speed);
    }
  }

  @Override
  public void testPeriodic() {
  }

  //Limelight Tracking Method
  public void Update_Limelight_Tracking(){
    //Tune values below
    final double STEER_K = 0.03; //how hard to turn toward the target
    final double DRIVE_K = 0.26; //how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 2.0; //area of the target when the robot reaches the wall
    final double MAX_DRIVE = 0.7; //speed for aiming

    //get current values from the limelight
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    if(tv < 1.0){
      m_LimelightHasValidTarget = false;
      m_LimelightDriveCommand = 0.0;
      m_LimelightSteerCommand = 0.0;
      return;
    }
    m_LimelightHasValidTarget = true;

    //Steering
    double steer_cmd = tx * STEER_K;
    m_LimelightSteerCommand = steer_cmd;

    //Drive foward until the target area reaches our desired area
    double drive_cmd = (DESIRED_TARGET_AREA - ta) * DRIVE_K;

    //Don't let the robot drive too fast into the goal
    if(drive_cmd > MAX_DRIVE){
      drive_cmd = MAX_DRIVE;
    }
    m_LimelightDriveCommand = drive_cmd;
  }

}
