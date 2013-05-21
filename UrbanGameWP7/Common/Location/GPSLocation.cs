using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Device.Location;

namespace Common
{
    public class GPSLocation
    {
        GeoCoordinateWatcher watcher;
        Action<GeoCoordinate> callback;
        object syncObj = new object();

        private void disposeWatcher()
        {
            lock (syncObj)
            {
                if (watcher != null)
                {
                    watcher.Dispose();
                    watcher = null;
                }
            }
        }

        private void watcher_StatusChanged(object sender, GeoPositionStatusChangedEventArgs e)
        {
            switch (e.Status)
            {
                case GeoPositionStatus.Disabled:
                    disposeWatcher();
                    break;

                case GeoPositionStatus.NoData:
                    disposeWatcher();
                    break;
            }
        }

        private void watcher_PositionChanged(object sender, GeoPositionChangedEventArgs<GeoCoordinate> e)
        {
            if (callback != null) callback(e.Position.Location);
            disposeWatcher();
        }

        public double CalculateDistance(GeoCoordinate currentPosition, double Latitude, double Longitude)
        {
            return currentPosition.GetDistanceTo(new GeoCoordinate(Latitude, Longitude)) / 1000.0;
        }

        public void GetCurrentCoordinates(Action<GeoCoordinate> callback)
        {
            this.callback = callback;

            disposeWatcher();
            watcher = new GeoCoordinateWatcher(GeoPositionAccuracy.Default) { MovementThreshold = 0 };
            watcher.PositionChanged += watcher_PositionChanged;
            watcher.StatusChanged += watcher_StatusChanged;
            watcher.Start();
        }
    }
}
