/**
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

define(['log', 'jquery', 'backbone', 'lodash', 'context_menu', 'mcustom_scroller', 'launch_manager', 'alerts'],

    function (log, $, Backbone, _, ContextMenu, mcustomScroller, LaunchManager, alerts) {

    var Launcher = Backbone.View.extend({

        initialize: function (config) {
            var errMsg;
            this._items = [];
            this.application = _.get(config, 'application');

	        //event bindings
//	        this._$parent_el.on('click',"#run_application", _.bindKey(this,'runApplication'));
//	        this._$parent_el.on('click',"#run_service", _.bindKey(this,'runService'));
//            this._$parent_el.on('click',"#stop_program", _.bindKey(this,'stopProgram'));
//
//            LaunchManager.on("execution-started",_.bindKey(this,'renderBody'));
//            LaunchManager.on("execution-ended",_.bindKey(this,'renderBody'));

        },

        debugApplication: function(){
            var activeTab = this.application.tabController.getActiveTab();
            if(this.isReadyToRun(activeTab)) {
                var file = activeTab.getFile();
                LaunchManager.debugApplication(file);
            } else {
                alerts.error("Save file before start debugging application");
            }
        },

        runService: function(){
        	var activeTab = this.application.tabController.getActiveTab();
        	if(this.isReadyToRun(activeTab)) {
        	    var file = activeTab.getFile();
       		    LaunchManager.runService(file);
        	} else {
                alerts.error("Save file before running service");
        	}
        },

        runApplication: function(){
        	var activeTab = this.application.tabController.getActiveTab();

        	// only file tabs can run application
        	if(this.isReadyToRun(activeTab)) {
        	    var file = activeTab.getFile();
        	    LaunchManager.runApplication(file);
        	} else {
        	    alerts.error("Save file before running application");
        	}
        },

        isReadyToRun: function(tab) {
            if (!typeof tab.getFile === "function") {
                return false;
            }

            var file = tab.getFile();
            // file is not saved give an error and avoid running
            if(file.isDirty()) {
                return false;
            }

            return true;
        },

        stopProgram: function(){
            LaunchManager.stopProgram();
        },        

        isActive: function(){
            return this._activateBtn.parent('li').hasClass('active');
        },

        render: function () {
            var self = this;
            var activateBtn = $(_.get(this._options, 'activateBtn'));
            this._activateBtn = activateBtn;

            var launcherContainer = $('<div role="tabpanel"></div>');
            launcherContainer.addClass(_.get(this._options, 'cssClass.container'));
            launcherContainer.attr('id', _.get(this._options, ('containerId')));
            this._$parent_el.append(launcherContainer);

            activateBtn.on('click', function(e){
                $(this).tooltip('hide');
                e.preventDefault();
                e.stopPropagation();
                self.application.commandManager.dispatch(_.get(self._options, 'command.id'));
            });

            activateBtn.attr("data-placement", "bottom").attr("data-container", "body");

            if (this.application.isRunningOnMacOS()) {
                activateBtn.attr("title", "Run (" + _.get(self._options, 'command.shortcuts.mac.label') + ") ").tooltip();
            } else {
                activateBtn.attr("title", "Run  (" + _.get(self._options, 'command.shortcuts.other.label') + ") ").tooltip();
            }

            this._verticalSeparator.on('drag', function(event){
                if( event.originalEvent.clientX >= _.get(self._options, 'resizeLimits.minX')
                    && event.originalEvent.clientX <= _.get(self._options, 'resizeLimits.maxX')){
                    self._verticalSeparator.css('left', event.originalEvent.clientX);
                    self._verticalSeparator.css('cursor', 'ew-resize');
                    var newWidth = event.originalEvent.clientX;
                    self._$parent_el.parent().width(newWidth);
                    self._containerToAdjust.css('padding-left', event.originalEvent.clientX);
                    self._lastWidth = newWidth;
                    self._isActive = true;
                }
                event.preventDefault();
                event.stopPropagation();
            });
            this._launcherContainer = launcherContainer;

            launcherContainer.mCustomScrollbar({
                theme: "minimal",
                scrollInertia: 0,
                axis: "xy"
            });
            if(!_.isEmpty(this._openedFolders)){
                this._openedFolders.forEach(function(folder){
                    self.createExplorerItem(folder);
                });
            }
            this.renderBody();
            return this;
        },


        renderBody : function(){
        	this._launcherContainer.html(this.compiled(LaunchManager));
        },

        showConsole : function(){
        	$("#tab-content-wrapper").css("height:70%");
        	$("#console-container").css("height:30%");
        }
    });

    return Launcher;

});

