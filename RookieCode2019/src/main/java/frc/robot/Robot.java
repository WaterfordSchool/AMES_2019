/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;

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

  //Motor Controllers
  Talon Left = new Talon(3); 
  Talon Right = new Talon(0);
  Talon CoconutGun = new Talon(7);
  DifferentialDrive tDrive = new DifferentialDrive (Left, Right);
  Joystick drivercontroller = new Joystick (0);
  Joystick operatorcontroller = new Joystick (1);
  double speed = 0.6;
  
  private double autoStartTime;
/** CoconutGun is the ball shooter/roller
as a tribute to Funky Kong, it is the coconut gun.
funky kong for those of you who dont know,
is a side character in the donkey kong country series
and his most recent appearance was in
Super Smash Brothers Ultimate (2018) as a spirit.
He does not use the coconut gun, however he is a hero of mine
and I personally think hes the coolest monkey ever.
ooh ooh ahh ahh ahh
*/

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
   if (timeElapsed < 4.5){ //drive forward for 6 seconds
     tDrive.tankDrive(-speed, -speed);
     }
    else if (timeElapsed < 12){
      CoconutGun.set(-0.6);
    }
    else {
      tDrive.tankDrive(0.0, 0.0);
      CoconutGun.set(0.0);
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  tDrive.tankDrive(drivercontroller.getRawAxis(1), drivercontroller.getRawAxis(3));
  CoconutGun.set(operatorcontroller.getY( ));
  
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
