using Common;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WebService.JsonConverters
{
    public class JsonGameTypeConverter : JsonConverter
    {
        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(GameType);
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {
            switch ((string)reader.Value)
            {
                case "shortest_time":
                    return GameType.Race;
                case "max_points":
                    return GameType.ScoreAttack;
            }

            throw new InvalidOperationException("JsonConverter: GameType unrecognized.");
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
            switch ((GameType)value)
            {
                case GameType.Race:
                    writer.WriteValue("shortest_time");
                    break;
                case GameType.ScoreAttack:
                    writer.WriteValue("max_points");
                    break;
            }   
        }
    }
}
