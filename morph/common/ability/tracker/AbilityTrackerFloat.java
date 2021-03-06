package morph.common.ability.tracker;

import morph.api.Ability;
import morph.common.ability.AbilityFloat;
import morph.common.entity.EntTracker;

public class AbilityTrackerFloat extends AbilityTracker 
{

	public double prevMotionY;
	public double terminalVelo;
	public int terminalTime;
	
	public AbilityTrackerFloat(EntTracker tracker, String ability) 
	{
		super(tracker, ability);
		prevMotionY = 10D;
		terminalVelo = -10D;
	}

	@Override
	public void initialize() 
	{
		if(entTracker.simulated)
		{
			entTracker.trackedEnt.setPosition(entTracker.trackedEnt.posX, entTracker.trackedEnt.posY + 15D, entTracker.trackedEnt.posZ);
		}
	}

	@Override
	public void trackAbility() 
	{
		if(entTracker.trackedEnt.onGround)
		{
			terminalTime = 0;
		}
		if(!entTracker.trackedEnt.onGround && !entTracker.trackedEnt.isCollidedHorizontally)
		{
			if(prevMotionY != entTracker.trackedEnt.motionY && Math.abs(entTracker.trackedEnt.motionY - prevMotionY) > 0.001D)
			{
				prevMotionY = entTracker.trackedEnt.motionY;
				terminalTime = 0;
			}
			else
			{
				terminalTime++;
				if(terminalTime >= 10 && prevMotionY < -0.05D)
				{
					terminalVelo = entTracker.trackedEnt.motionY;
					setHasAbility(true);
					if(entTracker.trackTimer > 5)
					{
						entTracker.trackTimer = 5;
					}
				}
			}
		}
	}

	@Override
	public Ability createAbility() 
	{
		return new AbilityFloat(terminalVelo, true);
	}

	@Override
	public int trackingTime() 
	{
		return entTracker.simulated ? 40 : 200; // 20s
	}

}
