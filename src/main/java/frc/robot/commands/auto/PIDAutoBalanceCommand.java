package frc.robot.commands.auto;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

import static frc.robot.Constants.AutoValues.PITCH_CONTROLLER;
import static frc.robot.commands.auto.PIDTargetCommand.inTolerance;

public class PIDAutoBalanceCommand extends Command {

    public PIDAutoBalanceCommand() {
        addRequirements(Robot.swerveDrive);
    }

    @Override
    public void execute() {
        Robot.swerveDrive.autoDrive(
                MathUtil.clamp(
                        -PITCH_CONTROLLER.calculate(Robot.swerveDrive.getGyroRoll(), 0), -0.3, 0.3
                ),
                0,
                0
        );
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.swerveDrive.stop();
    }
}
