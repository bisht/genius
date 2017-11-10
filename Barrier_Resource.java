/* 
Sample class that allows event procesing logic to wait till *ALL* N dependent resources become active.
This is only a sample class. The goal of this classs it to illustrate the basic concepts. 

Terminology:
'Events' indicate that addition/deletion/modification of a 'resource' such as port-add, port-delete etc.

Assumption:
Event processing is solely dependent on state of various other resources.

Key Ideas:
There are two main ideas
1. For event processing, store *all* the data required for event processing witin application itself.
In particular, the application should not access external datastore to get latest data. 
This data is assumed to be state of various other resources in the system.
2. The modification of this data (via events) and associated processing is seralized within an object lock. 
The lock allows consistency of this data during event processing.

The processing constructs are
- Activate (which is trigerred when there is transition from all-resources-not-active to all-resources-active)
- De-activate (which is trigerred when there is transition from all-resources-active to all-resources-not-active)
- Update (which is triggered when one of the resource is modified in all-resources-active state)

Dataplane Considerations for updates:
In cases where event processing results in provisioning of dataplane network state, following should be ensured for update processing:
Suppose initial aggregate state of all resources (maintained by *an object* of this class) is S1
At a later time, this state is modified to state S2 via modification M of a dependent resource state.
Function Activate(S2) MUST provision an external network state that is identical to Activate(S1) followed by Update(M)

In a multi-threaded distributed system, the resouce values can change during the event processing. 
The class tries to solve this problem by storing a immutable value of resource state (when resource is activated via member function)
These immutable values are passed to three processing handlers.

Typical Usage:
The typical usage expected is following:
1. This class objects are 'value' part in key-value map. The identification of key is left to application. 
In case of Genius module, this can be trunk-port-id.
2. These Barrier_Resources objects are created on all three nodes of the cluster (using say clustered data change listeners).
Maintenance of resource states on all nodes is required to support hot-standby functionality (instead of warm standby service). 

Singleton Processing:
For many modules (including Genius), the event procesing is done only one 'designated' node. 
This can be implemented by ensuring the processing handler are run on of the 'designated' nodes.
The mechanism by which this 'designated' node is chosen is left to application. Two typical ways are:
1. Ensuring that one of the resources becomes available only on one node using DTCL (instead of CDTCL)
2. Using Entity-Owner-Service to choose the handler processing node in the cluster.

Links:
This class is inspired from CyclicBarrier Class. The differences from Cyclic Barrier class are the following:
1. in CyclicBarrier class, the N visitor threads are blocked. 
In this class running of handlers is blocked. The enqueing thread only stores a copy/updates state of one of N resources.
The enqueuing thread itself is not blocked.
2. Unlike CyclicBarrier class which has only one handler, this class has three handlers to support add/del/mod
3. Unlike CyclicBarrier class where barrier has to be explicitly reset, this class resets the barrier automatically.
*/

Class Barrier_resource {
private boolean resource[N]; // array that stores information if resource-i is active
private Object resource_data[N]; //optional data for resource-i

private boolean barrier_active = TRUE; // true as long as one of the resources is inactive

//updates barrier when a resource is activated. Runs add-handler is there is trasition to all-active
private boolean update_barrier_on_add (int index)  
{
	// check if this this enabled resource will cause barrier to go-down
	for (i = 0; i < N; i++) {
	 	if (resource[i] == FALSE) return FALSE;
	}	

	// if you reach here.. it means there is a transition since all resources are now available.
	barrier_active = FALSE;
	if (process_handlers() == TRUE) {
		//main fucntion to run that does actual processing when all resources are available. 
		process_all_resources_up(index, resource_data); 
		return TRUE;
	}
	return FALSE;
}

//updates barrier when a resource is deactivated. Runs del-handler is there is trasition from all-active
private boolean update_barrier_on_del (int index)
{
	if (barrier_active == TRUE) //barrier was already setup.. nothing to be done
		return FALSE;

	//if you reach here, it means there is transition since all resources are *not* active now
	barrier_active = TRUE;
	
	if (process_resources() == TRUE) {
		//main function to run that does actual processing when all resources are not available. 
		process_some_resource_down(index, resource_data);  
		return TRUE;
	}
	return FALSE;
}

//Runs update-handler if state is all-active
private boolean update_barrier_on_mod(int index, Object prev_data)
{
	if (barrier_active == TRUE) // barrier already up.. nothing to be done
		return FALSE;

	//if you reach here, it means barrier was down.. and there is change in data.. so process it
	if (process_resources() == TRUE) {
		//main function to run that does actual processing when there is update to resources. 
		process_some_resource_modified(index, resource_data, prev_data);
		return TRUE;
	}
	return FALSE;
}

//synchronized meothod - take lock on resource object before changing the states
synchronized public int activate_resource (int resource_index, Object resource_data)
{
	Object resource_data_prev;
		
	if (resource[resource_index] == FALSE) {
		//activate resource scenario
		resource[resource_index] = TRUE;
		resource_data[resource_index] = resource_data;
		update_barrier_on_add(resource_index);
		return TRUE;
	} else {
		//update resource scenario
    	//take a copy of prev state.. since update may require prev state and new state
		resource_data_prev = copy_single_resource_data(resource_index, resource_data);
		resource_data[resource_index] = resource_data;
		update_barrier_on_modify(resource_index, resource_data_prev);
		return TRUE;
	}
	return FALSE;
}

//synchronized meothod - take lock on resource object before changing the states
synchronized public int deactivate_resource(int resource_index)
{
	if (resource[resource_index] == TRUE) {
		resource[resource_index] = FALSE;
		update_barrier_on_del(resource_index);
		return TRUE;
	} 
	return FALSE;
}

boolean public process_resources (int key)
{
	//some logic to check if this node should be processing resources.
	//Later on this can be used to do application level partitioning based on resource key
}

}
