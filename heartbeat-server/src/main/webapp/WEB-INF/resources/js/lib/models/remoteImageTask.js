define([], function() {
    var remoteImageTask = function(uuid, serialNumber, mac, ipAddress, imageName ) {
        var self = this;
        self.uuid = uuid;
        self.imageName = imageName;
        self.serialNumber = serialNumber;
        self.mac = mac;
        self.ipAddress = ipAddress;
        self.imageName = imageName;  
    };
    return remoteImageTask;
});

