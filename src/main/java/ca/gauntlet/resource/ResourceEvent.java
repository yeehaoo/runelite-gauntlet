package ca.gauntlet.resource;

import lombok.Value;

@Value
class ResourceEvent
{
	Resource resource;
	int count;
}
