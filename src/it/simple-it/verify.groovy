File f1 = new File( basedir, "target/site/set1/data1.png" );
assert f1.isFile()

File f2 = new File( basedir, "target/site/set1/data2.png" );
assert f2.isFile()

File l1 = new File( basedir, "target/site/set1/subdir/file1.png" );
assert l1.isFile()

File l2 = new File( basedir, "target/site/set1/subdir/file2.png" );
assert l2.isFile()

File o1 = new File( basedir, "target/site/set2/other.png" );
assert o1.isFile()

