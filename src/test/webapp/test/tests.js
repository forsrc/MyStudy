
"use strict";

QUnit.test( "hello test", function( assert ) {
    assert.ok( 1 == "1", "Passed!" );
});

QUnit.module("test[ ? + ? = ? ]", function (hooks) {

    QUnit.test("1 + 1 = ? ", function (assert) {

        var actual = 1 + 1;
        var expected = 2;

        assert.equal(actual, expected, actual);
    });

    QUnit.test("75 + 77 = ? ", function (assert) {

        var actual = 75 + 77;
        var expected = 152;

        assert.equal(actual, expected, actual);
    });
});