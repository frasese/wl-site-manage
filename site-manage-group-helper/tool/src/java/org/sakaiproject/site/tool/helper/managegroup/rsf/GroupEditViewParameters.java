/**********************************************************************************
 * $URL:$
 * $Id:$
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.site.tool.helper.managegroup.rsf;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * 
 * @author
 *
 */
public class GroupEditViewParameters extends SimpleViewParameters {

    public String id;

    public GroupEditViewParameters(String id) {
		super();
		this.id = id;
	}
    
    public GroupEditViewParameters() {
		super();
	}
    
	public GroupEditViewParameters(String viewId, String id) {
		this.id= id;
		this.viewID = viewId;
	}
    
    public String getId()
    {
    	return id;
    }

	public void setId(String id) {
		this.id = id;
	}
    
}
