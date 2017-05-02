FOR /L %%A IN (1,1,10000) DO (
  START /W java -jar -Xmx1g C:\Users\Jan\Desktop\parser\parser-jar-with-dependencies.jar -Verb RunAs
)