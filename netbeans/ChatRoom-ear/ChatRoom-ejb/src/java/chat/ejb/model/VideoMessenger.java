/* 
 * Copyright 2017 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package chat.ejb.model;

import javax.servlet.http.Part;

/**
 * This interface provides the function for relaying results of a video upload
 * execution.
 *
 * @author Bryan Daniel
 */
public interface VideoMessenger {

    /**
     * This method sends a message concerning the upload results of the video
     * associated with the given number.
     *
     * @param videoNumber the identifying video number
     */
    public void relayResults(Long videoNumber);
    
    /**
     * Get the video to upload
     *
     * @return the value of videoToUpload
     */
    public Part getVideoToUpload();
}
